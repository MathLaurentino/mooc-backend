package ifpr.edu.br.mooc.dto.pageable;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> conteudo,
        int paginaAtual,
        int tamanhoPagina,
        long totalElementos,
        int totalPaginas,
        boolean primeira,
        boolean ultima
) {
    public PageResponse(Page<T> page) {
        this(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isFirst(),
            page.isLast()
        );
    }
}