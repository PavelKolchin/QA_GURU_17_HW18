package guru_qa.models.lombok;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class Data1 {
    public Integer id;
    public String email;
    @JsonProperty("first_name")
    public String firstName;
    @JsonProperty("last_name")
    public String lastName;
    public String avatar;
}
