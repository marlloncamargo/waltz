package com.khartec.waltz.data.software_catalog;

import com.khartec.waltz.common.Checks;
import com.khartec.waltz.model.software_catalog.ImmutableSoftwarePackage;
import com.khartec.waltz.model.software_catalog.MaturityStatus;
import com.khartec.waltz.model.software_catalog.SoftwarePackage;
import com.khartec.waltz.schema.tables.records.SoftwarePackageRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.khartec.waltz.schema.tables.SoftwarePackage.SOFTWARE_PACKAGE;

@Repository
public class SoftwarePackageDao {

    private static final Logger LOG = LoggerFactory.getLogger(SoftwarePackageDao.class);


    private static final Function<SoftwarePackage, SoftwarePackageRecord> TO_RECORD = sp -> {
        SoftwarePackageRecord record = new SoftwarePackageRecord();

        record.setVendor(sp.vendor());
        record.setName(sp.name());
        record.setVersion(sp.version());
        record.setDescription(sp.description());

        record.setMaturityStatus(sp.maturityStatus().name());
        record.setNotable(sp.isNotable());

        record.setProvenance(sp.provenance());
        record.setExternalId(sp.externalId().orElse(null));

        return record;
    };


    private static final RecordMapper<Record, SoftwarePackage> TO_DOMAIN = r -> {
        SoftwarePackageRecord record = r.into(SOFTWARE_PACKAGE);
        return ImmutableSoftwarePackage.builder()
                .id(record.getId())
                .vendor(record.getVendor())
                .name(record.getName())
                .version(record.getVersion())
                .description(record.getDescription())
                .isNotable(record.getNotable())
                .maturityStatus(MaturityStatus.valueOf(record.getMaturityStatus()))
                .externalId(Optional.ofNullable(record.getExternalId()))
                .provenance(record.getProvenance())
                .build();
    };


    private final DSLContext dsl;

    @Autowired
    public SoftwarePackageDao(DSLContext dsl) {
        this.dsl = dsl;
    }


    public int[] bulkStore(Collection<SoftwarePackage> softwarePackages) {
        Checks.checkNotNull(softwarePackages, "Cannot store a null collection of software packages");

        List<SoftwarePackageRecord> records = softwarePackages.stream()
                .map(TO_RECORD)
                .collect(Collectors.toList());

        LOG.info("Bulk storing " + records.size() + " records");
        return dsl.batchStore(records).execute();
    }


    public List<SoftwarePackage> findByIds(Long... ids) {
        return findByCondition(SOFTWARE_PACKAGE.ID.in(ids));
    }

    public List<SoftwarePackage> findByIds(Collection<Long> ids) {
        return findByCondition(SOFTWARE_PACKAGE.ID.in(ids));
    }


    public List<SoftwarePackage> findByExternalIds(String... externalIds) {
        return findByCondition(SOFTWARE_PACKAGE.EXTERNAL_ID.in(externalIds));
    }

    public List<SoftwarePackage> findAll() {
        return findByCondition(DSL.trueCondition());

    }
    // -----

    private List<SoftwarePackage> findByCondition(Condition condition) {
        return dsl.select(SOFTWARE_PACKAGE.fields())
                .from(SOFTWARE_PACKAGE)
                .where(condition)
                .fetch(TO_DOMAIN);
    }

}
