package utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@JsonIgnoreProperties({"pageable"})
public class CustomizedPage<T> extends PageImpl<T> {

    public CustomizedPage() {
        super(List.of());
    }

    public CustomizedPage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
