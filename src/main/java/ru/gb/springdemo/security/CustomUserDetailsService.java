package ru.gb.springdemo.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.springdemo.model.AppUser;
import ru.gb.springdemo.model.Role;
import ru.gb.springdemo.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser currentUser = userRepository.findUserByName(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
		List<GrantedAuthority> authorityList = new ArrayList<>();
		for (Role role : currentUser.getRoleList()) {
			authorityList.add(new SimpleGrantedAuthority(role.getName()));
		}
		return new User(currentUser.getName(), currentUser.getPassword(), authorityList);
	}

	@PostConstruct
	public void addAdmin() {
		AppUser user = new AppUser();
		user.setName("admin");
		user.setPassword(new BCryptPasswordEncoder().encode("admin"));
		user.setRoleList(List.of(new Role("admin")));
		userRepository.save(user);
		userRepository.flush();
	}
}
