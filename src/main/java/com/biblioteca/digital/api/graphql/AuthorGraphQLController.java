package com.biblioteca.digital.api.graphql;

import com.biblioteca.digital.application.dto.AuthorDto;
import com.biblioteca.digital.application.dto.input.CreateAuthorInput;
import com.biblioteca.digital.application.dto.input.UpdateAuthorInput;
import com.biblioteca.digital.application.usecase.AuthorUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorGraphQLController {

    private final AuthorUseCase authorUseCase;

    @QueryMapping
    public List<AuthorDto> authors() {
        return authorUseCase.findAll();
    }

    @QueryMapping
    public AuthorDto authorById(@Argument Long id) {
        return authorUseCase.findById(id);
    }

    @QueryMapping
    public List<AuthorDto> authorsByNationality(@Argument String nationality) {
        return authorUseCase.findByNationality(nationality);
    }

    @MutationMapping
    public AuthorDto createAuthor(@Argument @Valid CreateAuthorInput input) {
        return authorUseCase.create(input);
    }

    @MutationMapping
    public AuthorDto updateAuthor(@Argument Long id, @Argument @Valid UpdateAuthorInput input) {
        return authorUseCase.update(id, input);
    }

    @MutationMapping
    public Boolean deleteAuthor(@Argument Long id) {
        return authorUseCase.delete(id);
    }
}
