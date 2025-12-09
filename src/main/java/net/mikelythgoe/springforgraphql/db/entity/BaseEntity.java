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

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @UuidV7
    protected UUID id;

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
