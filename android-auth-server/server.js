const express = require('express');
const mysql = require('mysql');
const app = express();
const port = 3000; 

// ⭐️ Google 인증 라이브러리 import ⭐️
const { OAuth2Client } = require('google-auth-library');
const GOOGLE_CLIENT_ID_WEB = "YOUR_WEB_CLIENT_ID_FROM_GOOGLE_CLOUD"; 
const client = new OAuth2Client(GOOGLE_CLIENT_ID_WEB);

// 미들웨어 설정
const cors = require('cors');
app.use(express.json()); 
app.use(cors()); 

// ⚠️ DB 접속 정보: 본인의 MySQL 정보로 변경하세요!
const db = mysql.createConnection({
    host: 'localhost', 
    user: 'root', 
    password: '0740', 
    database: 'AndroidAuthDB' 
});

db.connect(err => {
    if (err) {
        console.error('MySQL 연결 실패: ' + err.stack);
        return;
    }
    console.log('MySQL 연결 성공. 서버 구동 시작.');
});

// ----------------------------------------------------------------------
// ⭐️ Google 토큰 검증 함수 ⭐️
// ----------------------------------------------------------------------

async function verifyGoogleToken(idToken) {
    try {
        const ticket = await client.verifyIdToken({
            idToken: idToken,
            audience: GOOGLE_CLIENT_ID_WEB, 
        });
        const payload = ticket.getPayload();
        
        return {
            googleId: payload['sub'], 
            email: payload['email'],
            name: payload['name'],
        };

    } catch (error) {
        console.error('Google Token verification failed:', error);
        return null;
    }
}


// ----------------------------------------------------------------------
// ⭐️ 1. 일반 회원가입 API (POST /api/v1/users/register) ⭐️
// ----------------------------------------------------------------------
app.post('/api/v1/users/register', (req, res) => {
    const { id, password, name: realName } = req.body; 

    if (!id || !password) {
        return res.status(400).json({ code: 400, message: '아이디와 비밀번호는 필수입니다.' });
    }
    
    const tempEmail = id + '@user.local'; 
    
    const query = 'INSERT INTO users (name, password, real_name, email) VALUES (?, ?, ?, ?)';

    db.query(query, [id, password, realName, tempEmail], (err, result) => {
        if (err) {
            console.error("회원가입 DB 오류: " + err.code);
            if (err.code === 'ER_DUP_ENTRY') {
                return res.status(400).json({ code: 400, message: '이미 존재하는 아이디입니다.' });
            }
            return res.status(500).json({ code: 500, message: '회원가입 실패 (서버 오류)' });
        }
        res.status(200).json({ code: 200, message: '회원가입 성공' });
    });
});

// ----------------------------------------------------------------------
// ⭐️ 2. 일반 로그인 API (POST /api/v1/users/login) ⭐️
// ----------------------------------------------------------------------
app.post('/api/v1/users/login', (req, res) => {
    const { name, password } = req.body;
    
    const query = 'SELECT password FROM users WHERE name = ?'; 
    
    db.query(query, [name], (err, results) => {
        if (err || results.length === 0) {
            return res.status(401).json({ code: 401, message: '로그인 실패: 아이디를 찾을 수 없습니다.' });
        }
        
        const storedPassword = results[0].password;
        
        if (storedPassword === password) { 
            return res.status(200).json({ code: 200, message: '로그인 성공', data: { accessToken: 'example_token_123' } });
        } else {
            return res.status(401).json({ code: 401, message: '로그인 실패: 비밀번호가 일치하지 않습니다.' });
        }
    });
});

// ----------------------------------------------------------------------
// ⭐️ 3. Google 로그인 API (POST /api/v1/auth/google) ⭐️
// ----------------------------------------------------------------------
app.post('/api/v1/auth/google', async (req, res) => {
    const { token } = req.body; 
    
    if (!token) {
        return res.status(400).json({ code: 400, message: '토큰이 누락되었습니다.' });
    }

    const userInfo = await verifyGoogleToken(token);
    
    if (!userInfo) {
        return res.status(401).json({ code: 401, message: '유효하지 않은 Google 토큰입니다.' });
    }
    
    const { googleId, email, name: googleDisplayName } = userInfo; 
    
    // DB 로직: Google ID로 사용자 검색
    const findQuery = 'SELECT name FROM users WHERE google_id = ?';
    
    db.query(findQuery, [googleId], (err, results) => {
        if (err) {
            console.error("Google DB 검색 오류: " + err);
            return res.status(500).json({ code: 500, message: '서버 내부 오류' });
        }
        
        if (results.length > 0) {
            // 3-1. 기존 사용자: 로그인 성공
            console.log(`[Google Login] 기존 사용자 로그인: ${email}`);
            return res.status(200).json({ code: 200, message: 'Google 로그인 성공', data: { accessToken: 'google_token_jwt_123' } });
            
        } else {
            // 3-2. 신규 사용자: 회원가입 처리 (5개 컬럼에 5개 값 전달)
            const signupQuery = 'INSERT INTO users (name, password, real_name, email, google_id) VALUES (?, ?, ?, ?, ?)';
            const newName = 'google_' + googleId.substring(0, 8); 
            const tempPassword = ''; 
            
            // ⭐️ 수정된 부분: 5개의 값 전달 ⭐️
            db.query(signupQuery, [newName, tempPassword, googleDisplayName, email, googleId], (err, result) => {
                if (err) {
                    console.error("Google Signup DB 저장 오류: " + err);
                    return res.status(500).json({ code: 500, message: '회원가입 실패 (DB 오류)' });
                }
                console.log(`[Google Signup] 신규 사용자 가입 및 로그인: ${email}`);
                return res.status(200).json({ code: 200, message: 'Google 회원가입 및 로그인 성공', data: { accessToken: 'google_signup_jwt_456' } });
            });
        }
    });
});


// 서버 실행
app.listen(port, () => {
    console.log(`Node.js 서버가 http://localhost:${port} 에서 실행 중입니다.`);
});