package com.viafluvial.srvusuario.adapters.out.persistence;

import com.viafluvial.srvusuario.adapters.out.persistence.mapper.AdminPersistenceMapper;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.AdminRepository;
import com.viafluvial.srvusuario.application.port.out.AdminRepositoryPort;
import com.viafluvial.srvusuario.domain.model.Admin;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AdminRepositoryAdapter implements AdminRepositoryPort {

    private final AdminRepository adminRepository;
    private final AdminPersistenceMapper adminMapper;

    public AdminRepositoryAdapter(AdminRepository adminRepository, AdminPersistenceMapper adminMapper) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
    }

    @Override
    public Admin save(Admin admin) {
        return adminMapper.toDomain(adminRepository.save(adminMapper.toEntity(admin)));
    }

    @Override
    public Optional<Admin> findById(UUID id) {
        return adminRepository.findById(id).map(adminMapper::toDomain);
    }

    @Override
    public Optional<Admin> findByUserId(UUID userId) {
        return adminRepository.findByUserId(userId).map(adminMapper::toDomain);
    }
}
