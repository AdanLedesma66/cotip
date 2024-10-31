package py.com.cotip.external.cotipdb;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import py.com.cotip.domain.commons.TipoProveedor;
import py.com.cotip.domain.port.out.CotipDbOutPort;
import py.com.cotip.external.cotipdb.model.CotipEntity;
import py.com.cotip.external.cotipdb.repository.CotipRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class CotipDbOutPortImpl implements CotipDbOutPort {

    private CotipRepository cotipRepository;

    @Override
    public List<CotipEntity> saveAllCotipEntity(List<CotipEntity> cotipEntities, TipoProveedor tipoProveedor) {

        cotipEntities.forEach(cotipEntity -> {

            cotipEntity.setId(UUID.randomUUID());
            cotipEntity.setProvider(tipoProveedor.getDescription());
            cotipEntity.setUploadDate(OffsetDateTime.now());

        });

        return cotipRepository.saveAll(cotipEntities);
    }
}
