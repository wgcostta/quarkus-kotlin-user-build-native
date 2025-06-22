// Script de inicialização do MongoDB
// Este script é executado quando o container do MongoDB é iniciado pela primeira vez

// Conectar ao banco de dados userdb
db = db.getSiblingDB('userdb');

// Criar usuário para a aplicação
db.createUser({
    user: 'quarkus',
    pwd: 'quarkus123',
    roles: [
        {
            role: 'readWrite',
            db: 'userdb'
        }
    ]
});

// Criar coleção de usuários com validação de schema
db.createCollection('users', {
    validator: {
        $jsonSchema: {
            bsonType: 'object',
            required: ['name', 'email', 'age', 'createdAt'],
            properties: {
                name: {
                    bsonType: 'string',
                    description: 'Nome do usuário deve ser uma string e é obrigatório'
                },
                email: {
                    bsonType: 'string',
                    pattern: '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$',
                    description: 'Email deve ter um formato válido e é obrigatório'
                },
                age: {
                    bsonType: 'int',
                    minimum: 1,
                    maximum: 150,
                    description: 'Idade deve ser um número inteiro entre 1 e 150'
                },
                createdAt: {
                    bsonType: 'date',
                    description: 'Data de criação deve ser uma data válida'
                }
            }
        }
    }
});

// Criar índice único para email
db.users.createIndex({ 'email': 1 }, { unique: true });

// Criar índice para nome (para busca)
db.users.createIndex({ 'name': 1 });

// Inserir alguns dados de exemplo
db.users.insertMany([
    {
        name: 'João Silva',
        email: 'joao.silva@email.com',
        age: 30,
        createdAt: new Date()
    },
    {
        name: 'Maria Santos',
        email: 'maria.santos@email.com',
        age: 25,
        createdAt: new Date()
    },
    {
        name: 'Pedro Oliveira',
        email: 'pedro.oliveira@email.com',
        age: 35,
        createdAt: new Date()
    }
]);

print('Banco de dados userdb inicializado com sucesso!');