package tdtu.edu.vn.payload;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResponseData {
    private Object data;
    private String message;
    public ResponseData(Builder builder) {
        this.data = builder.data;
        this.message = builder.message;
    }
    public static class Builder {
        private Object data;
        private String message;
        public Builder data(Object data) {
            this.data = data;
            return this;
        }
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        public ResponseData build() {
            return new ResponseData(this);
        }
    }
}
