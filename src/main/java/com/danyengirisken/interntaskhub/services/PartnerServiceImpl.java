package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.Partner;
import com.danyengirisken.interntaskhub.entity.dto.PartnerDto;
import com.danyengirisken.interntaskhub.entity.dto.PartnerRequest;
import com.danyengirisken.interntaskhub.exception.ResourceNotFoundException;
import com.danyengirisken.interntaskhub.repository.PartnerDao;
import com.danyengirisken.interntaskhub.repository.ProjectDao;
import com.danyengirisken.interntaskhub.repository.UserDao;
import com.danyengirisken.interntaskhub.security.UserContext;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Partner CRUD is mantigi. Partner, kullanicilarin ve projelerin bagli oldugu
 * tenant oldugu icin tum uclar yalnizca ADMIN'e aciktir.
 */
@Service
@Transactional
public class PartnerServiceImpl implements PartnerService {

    private final PartnerDao partnerDao;
    private final UserDao userDao;
    private final ProjectDao projectDao;
    private final UserContext userContext;

    public PartnerServiceImpl(PartnerDao partnerDao,
                              UserDao userDao,
                              ProjectDao projectDao,
                              UserContext userContext) {
        this.partnerDao = partnerDao;
        this.userDao = userDao;
        this.projectDao = projectDao;
        this.userContext = userContext;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartnerDto> findAll() {
        userContext.requireAdmin();
        return partnerDao.findAllByOrderByNameAsc().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PartnerDto findById(Long id) {
        userContext.requireAdmin();
        return toDto(getOrThrow(id));
    }

    @Override
    public PartnerDto save(PartnerRequest request) {
        userContext.requireAdmin();

        Partner partner = (request.getId() != null) ? getOrThrow(request.getId()) : new Partner();

        String code = request.getCode().trim().toUpperCase();
        partnerDao.findByCode(code).ifPresent(existing -> {
            if (!existing.getId().equals(request.getId())) {
                throw new DataIntegrityViolationException(
                        "Bu partner kodu zaten kullanılıyor: " + code);
            }
        });

        partner.setCode(code);
        partner.setName(request.getName().trim());
        partner.setUsingLanguage(
                (request.getUsingLanguage() == null || request.getUsingLanguage().isBlank())
                        ? "tr" : request.getUsingLanguage().trim());
        partner.setDescription(request.getDescription());
        partner.setActive(
                (request.getActive() == null || request.getActive().isBlank())
                        ? "1" : request.getActive());

        return toDto(partnerDao.save(partner));
    }

    @Override
    public void delete(Long id) {
        userContext.requireAdmin();
        Partner partner = getOrThrow(id);

        // Bagli kayit varsa FK hatasi yerine anlasilir bir mesaj don.
        if (!userDao.findByPartnerId(id).isEmpty()) {
            throw new DataIntegrityViolationException(
                    "Bu partnere bağlı kullanıcılar var, önce onları taşıyın.");
        }
        if (!projectDao.findByPartner_id(id).isEmpty()) {
            throw new DataIntegrityViolationException(
                    "Bu partnere bağlı projeler var, önce onları silin.");
        }

        partnerDao.delete(partner);
    }

    private Partner getOrThrow(Long id) {
        return partnerDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner bulunamadı: " + id));
    }

    private PartnerDto toDto(Partner partner) {
        return new PartnerDto(
                partner.getId(),
                partner.getCode(),
                partner.getName(),
                partner.getUsingLanguage(),
                partner.getDescription(),
                partner.getActive());
    }
}
