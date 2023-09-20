package ai.springmtcoding.config.auth;

import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User userPS = userRepository.findByUsername(username).orElseThrow(
            () -> new InternalAuthenticationServiceException("인증 실패")
        );
        return new LoginUser(userPS);
    }
}
