package id.ac.ui.cs.advprog.pandacare.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @Test
    void testApiResponseConstructor() {
        ApiResponse<String> response = new ApiResponse<>(true, "Success", "Data");
        
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).isEqualTo("Data");
    }
    
    @Test
    void testApiResponseSettersGetters() {
        ApiResponse<Integer> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Count updated");
        response.setData(42);
        
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Count updated");
        assertThat(response.getData()).isEqualTo(42);
    }
    
    @Test
    void testApiResponseDifferentTypeParameters() {
        // Test with String
        ApiResponse<String> stringResponse = new ApiResponse<>(true, "String data", "Hello");
        assertThat(stringResponse.getData()).isInstanceOf(String.class);
        
        // Test with Integer
        ApiResponse<Integer> intResponse = new ApiResponse<>(true, "Int data", 123);
        assertThat(intResponse.getData()).isInstanceOf(Integer.class);
        
        // Test with custom DTO
        ChatMessageDTO dto = ChatMessageDTO.builder().id(1L).content("Test").build();
        ApiResponse<ChatMessageDTO> dtoResponse = new ApiResponse<>(true, "DTO data", dto);
        assertThat(dtoResponse.getData()).isInstanceOf(ChatMessageDTO.class);
    }
    
    @Test
    void testApiResponseEquality() {
        ApiResponse<String> response1 = new ApiResponse<>(true, "Success", "Data");
        ApiResponse<String> response2 = new ApiResponse<>(true, "Success", "Data");
        ApiResponse<String> response3 = new ApiResponse<>(false, "Error", "Different");
        
        assertThat(response1).isEqualTo(response2);
        assertThat(response1).isNotEqualTo(response3);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        assertThat(response1.hashCode()).isNotEqualTo(response3.hashCode());
    }
}