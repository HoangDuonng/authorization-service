-- V1__init_schema.sql
BEGIN;

-- Bảng roles: Quản lý các vai trò
CREATE TABLE IF NOT EXISTS public.roles (
    id BIGSERIAL PRIMARY KEY,
    document_id UUID DEFAULT gen_random_uuid() UNIQUE NOT NULL,
    name VARCHAR(50) UNIQUE NOT NULL, 
    display_name VARCHAR(100) NOT NULL, 
    description TEXT, 
    is_system BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_role_name CHECK (name ~ '^[a-z0-9_]+$')
);

-- Bảng permissions: Quản lý quyền hệ thống
CREATE TABLE IF NOT EXISTS public.permissions (
    id BIGSERIAL PRIMARY KEY,
    document_id UUID DEFAULT gen_random_uuid() UNIQUE NOT NULL,
    name VARCHAR(100) UNIQUE NOT NULL, 
    display_name VARCHAR(100) NOT NULL, 
    description TEXT, 
    module VARCHAR(50) NOT NULL,
    action VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_permission_name CHECK (name ~ '^[a-z0-9_]+$'),
    CONSTRAINT check_permission_module CHECK (module ~ '^[a-z0-9_]+$'),
    CONSTRAINT check_permission_action CHECK (action ~ '^[a-z0-9_]+$')
);

-- Bảng role_permissions: Liên kết vai trò với quyền
CREATE TABLE IF NOT EXISTS public.role_permissions (
    role_id BIGINT NOT NULL,              
    permission_id BIGINT NOT NULL,        
    document_id UUID DEFAULT gen_random_uuid() UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    PRIMARY KEY (role_id, permission_id), 
    FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE, 
    FOREIGN KEY (permission_id) REFERENCES public.permissions(id) ON DELETE CASCADE 
);

-- Bảng user_roles: Liên kết người dùng với vai trò
CREATE TABLE IF NOT EXISTS public.user_roles (
    user_id BIGINT NOT NULL,              
    role_id BIGINT NOT NULL,              
    granted_by BIGINT,                    
    document_id UUID DEFAULT gen_random_uuid() UNIQUE NOT NULL,
    granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    PRIMARY KEY (user_id, role_id),   
    FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE
);

-- Tạo chỉ mục cho bảng roles
CREATE INDEX IF NOT EXISTS idx_roles_name ON public.roles(name);
CREATE INDEX IF NOT EXISTS idx_roles_created_at ON public.roles(created_at);
CREATE INDEX IF NOT EXISTS idx_roles_is_system ON public.roles(is_system);
CREATE INDEX IF NOT EXISTS idx_roles_document_id ON public.roles(document_id);

-- Tạo chỉ mục cho bảng permissions
CREATE INDEX IF NOT EXISTS idx_permissions_name ON public.permissions(name);
CREATE INDEX IF NOT EXISTS idx_permissions_module ON public.permissions(module);
CREATE INDEX IF NOT EXISTS idx_permissions_action ON public.permissions(action);
CREATE INDEX IF NOT EXISTS idx_permissions_module_action ON public.permissions(module, action);
CREATE INDEX IF NOT EXISTS idx_permissions_document_id ON public.permissions(document_id);

-- Tạo chỉ mục cho bảng role_permissions
CREATE INDEX IF NOT EXISTS idx_role_permissions_role_id ON public.role_permissions(role_id);
CREATE INDEX IF NOT EXISTS idx_role_permissions_permission_id ON public.role_permissions(permission_id);
CREATE INDEX IF NOT EXISTS idx_role_permissions_document_id ON public.role_permissions(document_id);

-- Tạo chỉ mục cho bảng user_roles
CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON public.user_roles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role_id ON public.user_roles(role_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_granted_by ON public.user_roles(granted_by);
CREATE INDEX IF NOT EXISTS idx_user_roles_document_id ON public.user_roles(document_id);

COMMIT; 
