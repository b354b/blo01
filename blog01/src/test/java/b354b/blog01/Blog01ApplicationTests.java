package b354b.blog01;

import com.fasterxml.jackson.databind.ObjectMapper;
import b354b.blog01.springbootdeveloper.domain.Article;
import b354b.blog01.springbootdeveloper.dto.AddArticleRequest;
import b354b.blog01.springbootdeveloper.dto.UpdateArticleRequest;
import b354b.blog01.springbootdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class Blog01ApplicationTests {

	@Test
	void contextLoads() {
	}

	@DisplayName("findAllArticles : 블로그 글 목록 조회에 성공한다.")
	@Test
	public void findAllArticles() throws Exception {
		// given
		final String url = "/api/articles";
		final String title = "title";
		final String content = "content";

		blogRepository.save(Article.builder()
				.title(title)
				.content(content)
				.build());

		// when
		final ResultActions resultActions = mockMvc.perform(get(url)
				.accept(MediaType.APPLICATION_JSON));

		// then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].content").value(content))
				.andExpect(jsonPath("$[0].title").value(title));

	}
}
