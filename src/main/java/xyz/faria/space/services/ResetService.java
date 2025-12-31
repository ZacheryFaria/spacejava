package xyz.faria.space.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.faria.space.models.Reset;
import xyz.faria.space.repositories.ResetRepository;
import xyz.faria.space.spaceapi.Utils;

@Service
@RequiredArgsConstructor
public class ResetService {

    private final ResetRepository resetRepository;

    public Reset getCurrentReset() {
        var currentDate = Utils.getCurrentResetDate();

        var currentReset = resetRepository.findByResetDate(currentDate);

        if (currentReset.isPresent()) {
            return currentReset.get();
        } else {
            var reset = new Reset();
            reset.setResetDate(currentDate);
            return resetRepository.save(reset);
        }
    }
}
