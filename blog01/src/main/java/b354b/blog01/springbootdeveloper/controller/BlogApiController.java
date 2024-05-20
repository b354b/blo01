package b354b.blog01.springbootdeveloper.controller;

import b354b.blog01.springbootdeveloper.repository.BlogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import b354b.blog01.springbootdeveloper.domain.Article;
import b354b.blog01.springbootdeveloper.dto.AddArticleRequest;
import b354b.blog01.springbootdeveloper.dto.ArticleResponse;
import b354b.blog01.springbootdeveloper.dto.UpdateArticleRequest;
import b354b.blog01.springbootdeveloper.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RequiredArgsConstructor
@RestController
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
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

    @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }


    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public class BlogApiController {

        private final BlogService blogService;

        @PostMapping("/api/articles")
        public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
            Article savedArticle = blogService.save(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(savedArticle);
        }

        @GetMapping("/api/articles")
        public ResponseEntity<List<ArticleResponse>> findAllArticles() {
            List<ArticleResponse> articles = blogService.findAll()
                    .stream()
                    .map(ArticleResponse::new)
                    .toList();

            return ResponseEntity.ok()
                    .body(articles);
        }

        @GetMapping("/api/articles/{id}")
        public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
            Article article = blogService.findById(id);

            return ResponseEntity.ok()
                    .body(new ArticleResponse(article));
        }

        @DeleteMapping("/api/articles/{id}")
        public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
            blogService.delete(id);

            return ResponseEntity.ok()
                    .build();
        }

        @PutMapping("/api/articles/{id}")
        public ResponseEntity<Article> updateArticle(@PathVariable long id,
                                                     @RequestBody UpdateArticleRequest request) {
            Article updatedArticle = blogService.update(id, request);

            return ResponseEntity.ok()
                    .body(updatedArticle);
        }

    }
}