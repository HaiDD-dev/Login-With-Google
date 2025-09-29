CREATE TABLE dbo.users (
id INT IDENTITY(1,1) PRIMARY KEY,
email VARCHAR(255) NULL,
username VARCHAR(100) NULL,
password_hash VARCHAR(255) NULL,
full_name NVARCHAR(100) NULL,
role VARCHAR(20) NOT NULL DEFAULT 'reader' CHECK (role IN ('reader','author','staff','admin')),
is_verified BIT NOT NULL DEFAULT 0,
status VARCHAR(10) NOT NULL DEFAULT 'active' CHECK (status IN ('active','banned','inactive')),
created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
GO

CREATE UNIQUE INDEX UX_users_email ON dbo.users(email) WHERE email IS NOT NULL;
CREATE UNIQUE INDEX UX_users_username ON dbo.users(username) WHERE username IS NOT NULL;
GO

CREATE TABLE dbo.oauth_accounts (
id INT IDENTITY(1,1) PRIMARY KEY,
user_id INT NOT NULL FOREIGN KEY REFERENCES dbo.users(id) ON DELETE CASCADE,
provider VARCHAR(50) NOT NULL, -- 'google'
provider_account_id VARCHAR(255) NOT NULL, -- sub (OpenID subject)

email VARCHAR(255) NULL,
email_verified BIT NULL,
name NVARCHAR(200) NULL,
given_name NVARCHAR(100) NULL,
family_name NVARCHAR(100) NULL,
picture NVARCHAR(512) NULL,
locale VARCHAR(16) NULL,
hd VARCHAR(255) NULL, -- hosted domain

-- Token & meta
scope NVARCHAR(512) NULL,
token_type VARCHAR(32) NULL,
access_token VARCHAR(2048) NULL,
refresh_token VARCHAR(2048) NULL,
id_token NVARCHAR(MAX) NULL,
token_expiry DATETIME2 NULL,
profile_refreshed_at DATETIME2 NULL DEFAULT SYSDATETIME(),
created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
updated_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),

CONSTRAINT UQ_oauth_provider UNIQUE (provider, provider_account_id)
);
GO

CREATE TABLE dbo.products (
id INT IDENTITY(1,1) PRIMARY KEY,
name NVARCHAR(200) NOT NULL,
description NVARCHAR(MAX) NULL,
price DECIMAL(18,2) NOT NULL,
stock INT NOT NULL DEFAULT 0,
created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
updated_at DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
GO

-- Sample
INSERT INTO dbo.products(name, description, price, stock)
VALUES
(N'...', N'...', 59.90, 10),
(N'...', N'...', 25.00, 50);
