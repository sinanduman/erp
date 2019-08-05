package erp.irfan.entity;

import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@Data
public class Greeting implements Serializable {
    private String greeting = "Hello World!";
}
