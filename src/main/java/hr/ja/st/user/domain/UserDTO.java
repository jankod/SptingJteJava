package hr.ja.st.user.domain;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDTO {

    String id;
    String username;
}
