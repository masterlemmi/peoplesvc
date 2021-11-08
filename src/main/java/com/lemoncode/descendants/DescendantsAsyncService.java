package com.lemoncode.descendants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class DescendantsAsyncService {
    private final DescendantsService descendantsService;
    private final AncestryRepository ancestryRepository;

    private final AtomicBoolean alreadyRunning = new AtomicBoolean(false);

    @Async
    public void recreateDescendants() {
        if (!alreadyRunning.get()) {
            alreadyRunning.set(true);
            List<Ancestry> ancestryList = ancestryRepository.findAll();
            ancestryList.forEach(x ->
                    descendantsService.createAncestry(x.getId(), x.getLabel()
                    ));
            alreadyRunning.set(false);
        } else {
            log.info("ANcestry recreation is in progress. Other request will be ignored.");
        }

    }

}