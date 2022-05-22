package com.contentparser.scheduler;

import com.common.entity.RawData;
import com.common.enums.State;
import com.common.repository.RawDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.common.utils.Constants.wbApiPrefix;

/**
 * Class that initialising all system
 *
 */
@Component
@RequiredArgsConstructor
public class Init {

    @Autowired
    private RawDataRepository rawDataRepo;

    @PostConstruct
    public void testCategoryDownload() {
        //rawDataRepo.save(new RawData(wbApiPrefix + "/api/catalog/sport/vidy-sporta/velosport", State.QUEUED));
        //rawDataRepo.save(new RawData(wbApiPrefix + "/api/menu/getburger?includeBrands=False", wbApiPrefix, State.QUEUED));
        rawDataRepo.save(new RawData(wbApiPrefix + "/api/catalog/aksessuary/avtotovary", wbApiPrefix, State.QUEUED));
    }
}
