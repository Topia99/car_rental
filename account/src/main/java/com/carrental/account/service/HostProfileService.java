package com.carrental.account.service;

import com.carrental.account.doa.AccountRepository;
import com.carrental.account.doa.HostProfileRepository;
import com.carrental.account.dto.HostProfileResponse;
import com.carrental.account.dto.HostProfileUpdateRequest;
import com.carrental.account.model.Account;
import com.carrental.account.model.HostProfile;
import com.carrental.account.model.HostStatus;
import com.carrental.account.model.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@Transactional
public class HostProfileService {
    private final HostProfileRepository hostRepo;
    private final AccountRepository accountRepo;

    public HostProfileService(HostProfileRepository hostRepo, AccountRepository accountRepo){
        this.hostRepo = hostRepo;
        this.accountRepo = accountRepo;
    }

    /** Add HOST role and create HostProfile if absent. Call when user opts into hosting. */
    public HostProfileResponse enableHosting(Long userId){
        Account acc = accountRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        acc.getRoles().add(Role.HOST); // get the role set and add role HOST to the set

        HostProfile hp = hostRepo.findByUser_id(userId)
                .orElseGet(() ->{
                    HostProfile np = new HostProfile();
                    np.setUser(acc);
                    np.setStatus(HostStatus.PENDING);
                    return hostRepo.save(np);
                });
        return to(hp);
    }

    public HostProfileResponse getForUser(Long userId){
        HostProfile hp = hostRepo.findByUser_id(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Host Profile not found"));

        return to(hp);
    }

    public HostProfileResponse UpdateOrInsertForUser(Long userId, HostProfileUpdateRequest req) {
        HostProfile hp = hostRepo.findByUser_id(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Host Profile not found"));

        if (req.displayName()!=null) hp.setDisplayName(req.displayName());
        if (req.about()!=null) hp.setAbout(req.about());
        if (req.defaultPickupInstructions()!=null) hp.setDefaultPickupInstructions(req.defaultPickupInstructions());

        hostRepo.save(hp);

        return to(hp);
    }

    /** Admin-only action */
    public HostProfileResponse setStatus(Long userId, HostStatus status){
        HostProfile hp = hostRepo.findByUser_id(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Host profile not found"));
        hp.setStatus(status);
        return to(hp);
    }

    // --- Helper method ---
    private HostProfileResponse to(HostProfile hp){
        return new HostProfileResponse(
                hp.getId(),
                hp.getUser().getId(),
                hp.getStatus().name(),
                hp.getDisplayName(),
                hp.getAbout(),
                hp.getDefaultPickupInstructions()
        );
    }
}
