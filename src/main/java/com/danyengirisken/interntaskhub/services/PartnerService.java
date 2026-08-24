package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.dto.PartnerDto;
import com.danyengirisken.interntaskhub.entity.dto.PartnerRequest;
import java.util.List;

public interface PartnerService {

    List<PartnerDto> findAll();

    PartnerDto findById(Long id);

    PartnerDto save(PartnerRequest request);

    void delete(Long id);
}
