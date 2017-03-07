package org.asaunin.socialnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword implements Serializable{

	@NotNull
	@Size(min = 5, max = 50)
	private String currentPassword;

	@NotNull
	@Size(min = 5, max = 50)
	private String password;

}
