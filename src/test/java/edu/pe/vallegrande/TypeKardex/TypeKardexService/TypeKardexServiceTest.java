package edu.pe.vallegrande.TypeKardex.service;

import edu.pe.vallegrande.TypeKardex.model.TypeKardex;
import edu.pe.vallegrande.TypeKardex.repository.TypeKardexRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class TypeKardexServiceTest {

    @Mock
    private TypeKardexRepository repository;

    @InjectMocks
    private TypeKardexService service;

    private TypeKardex typeKardex;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        typeKardex = new TypeKardex();
        typeKardex.setId(1L);
        typeKardex.setName("Sample Kardex");
        typeKardex.setMaximumAmount(100);
        typeKardex.setMinimumQuantity(10);
        typeKardex.setProductId(1L);          // Long
        typeKardex.setSupplierId(2);           // Integer (sin sufijo L)
        typeKardex.setShedId(3L);              // Long
        typeKardex.setStatus("A");
        typeKardex.setDescription("Test");
    }

    @Test
    void testCreate() {
        when(repository.save(typeKardex)).thenReturn(Mono.just(typeKardex));

        StepVerifier.create(service.create(typeKardex))
                .expectNext(typeKardex)
                .verifyComplete();
    }

    @Test
    void testUpdate() {
        when(repository.findById(1L)).thenReturn(Mono.just(typeKardex));
        when(repository.save(any(TypeKardex.class))).thenReturn(Mono.just(typeKardex));

        StepVerifier.create(service.update(1L, typeKardex))
                .expectNext(typeKardex)
                .verifyComplete();
    }

    @Test
    void testDeleteLogical() {
        when(repository.findById(1L)).thenReturn(Mono.just(typeKardex));
        when(repository.save(any(TypeKardex.class))).thenReturn(Mono.just(typeKardex));

        StepVerifier.create(service.deleteLogical(1L))
                .verifyComplete();

        verify(repository).save(argThat(kardex -> kardex.getStatus().equals("I")));
    }

    @Test
    void testRestore() {
        typeKardex.setStatus("I");
        when(repository.findById(1L)).thenReturn(Mono.just(typeKardex));
        when(repository.save(any(TypeKardex.class))).thenReturn(Mono.just(typeKardex));

        StepVerifier.create(service.restore(1L))
                .verifyComplete();

        verify(repository).save(argThat(kardex -> kardex.getStatus().equals("A")));
    }

    @Test
    void testDeletePhysical() {
        when(repository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(service.deletePhysical(1L))
                .verifyComplete();
    }
}
