db.createUser(
    {
        user: "user",
        pwd: "pass",
        roles: [ { role: "readWrite", db: "jdbc_authentication"} ],
        passwordDigestor: "server"
    }
)
db.user.insertMany([
    {

        username: 'user',
        password: '$2a$10$mIb/bICwTrcZNgn0wB565ev3lM7mOePDS/mliTYc0lepext6n7F.a',
        enabled: true,
        roles:[{'role':'ROLE_USER'}]
    },
    {

        username: 'user2',
        password: '$2a$10$mIb/bICwTrcZNgn0wB565ev3lM7mOePDS/mliTYc0lepext6n7F.a',
        enabled: true,
        roles:[{'role':'ADMIN_USER'}]
    },
    {

        username: 'admin',
        password: '$2a$10$CNPpPTLGLNvO.O4t/Io5S.JZYpI7/X/5kiORP0VcKnKLnIRb3CLSe',
        enabled: true
    }
])