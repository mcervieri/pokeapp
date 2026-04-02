INSERT INTO users (username, email, password_hash, created_at)
VALUES (
    'mcervieri',
    'mcervieri@test.com',
    '$2a$10$gzd4UUPWChkFub0tIKxBT.oe3kLHmdd2lcsOmp9MylE.7ALDgSYwa',
    NOW()
)
ON CONFLICT (username) DO NOTHING;