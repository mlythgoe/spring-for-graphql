package net.mikelythgoe.springforgraphql.db.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import net.mikelythgoe.springforgraphql.db.idgenerator.UuidV7Generator;
import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.UUID;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Base entity class holds the UUID 7 identifier for all entities.
 */
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * The unique identifier
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @UuidV7
    protected UUID id;

    /**
     * Default constructor for the BaseEntity class.
     * <p>
     * This constructor is protected to ensure that it is only accessible within the class itself,
     * subclasses, or within the same package. It is primarily required for use by JPA and similar
     * ORM frameworks that rely on a no-args constructor for entity instantiation.
     */
    protected BaseEntity() {
    }

    protected BaseEntity(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    /**
     * Custom annotation to replace the deprecated @GenericGenerator.
     * This encapsulates the configuration for the UUIDv7 generator.
     */
    @IdGeneratorType(UuidV7Generator.class)
    @Retention(RUNTIME)
    @Target({FIELD, METHOD})
    public @interface UuidV7 {
    }
}
