package br.com.alpha7.server.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * <b>Descrição:</b><br>
 * Representa um livro cadastrado no sistema.
 * <br>
 * Contém informações essenciais como ISBN, título, data de publicação
 * e relacionamentos com autores e editoras.
 * <p>
 * <b>Regras de negócio:</b>
 * <ul>
 * 	<li>O ISBN é obrigatório e deve ser único.</li>
 * 	<li>O título do livro é obrigatório.</li>
 * 	<li>A data de registro é preenchida automaticamente no momento da persistência.</li>
 * </ul>
 * 
 * @author Fernando Kramer De Souza.
 * @since 1.0.0
 * 
 */
@Entity
@Table(name = "books")
public class Book {

	/**
     * Identificador único do livro.
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    /**
     * Código ISBN do livro.
     * <p>
     * Deve ser único e não pode ser nulo.
     */
	@Column(name = "isbn", nullable = false, unique = true)
	private String isbn;
	
    /**
     * Título do livro.
     * <p>
     * Campo obrigatório.
     */
	@Column(name = "title", nullable = false)
	private String title;
	
    /**
     * Lista de autores associados ao livro.
     * <p>
     * Relacionamento muitos-para-muitos com {@link Author}.
     */
	@ManyToMany
	@JoinTable(
			name = "book_author",
			joinColumns = @JoinColumn(name = "id_book"),
			inverseJoinColumns = @JoinColumn(name = "id_author")
	)
	private List<Author> authors = new ArrayList<>();;
	
    /**
     * Lista de editoras associadas ao livro.
     * <p>
     * Relacionamento muitos-para-muitos com {@link Publisher}.
     */
	@ManyToMany
	@JoinTable(
			name = "book_publisher",
			joinColumns = @JoinColumn(name = "id_book"),
			inverseJoinColumns = @JoinColumn(name = "id_publisher")
	)
	private List<Publisher> publishers = new ArrayList<>();
	

    /**
     * Data de publicação do livro.
     * <p>
     * Pode ser nula caso não informada.
     */
	@Column(name = "publication_date")
	private LocalDate publicationDate;
	
    /**
     * Data e hora de registro do livro no sistema.
     * <p>
     * Valor atribuído automaticamente antes da persistência.
     */
	@Column(name = "registration_date", nullable = false)
	private LocalDateTime registrationDate;
	
	public Book() {
	}
	
    /**
     * Callback executado automaticamente antes da entidade ser persistida.
     * <p>
     * Responsável por definir a data e hora de registro do livro.
     *
     * @since 1.0
     */
	@PrePersist
	private void prePersist() {
		this.registrationDate = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getTitle() {
		return title;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public List<Publisher> getPublishers() {
		return publishers;
	}

	public LocalDate getPublicationDate() {
		return publicationDate;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPublicationDate(LocalDate publicationDate) {
		this.publicationDate = publicationDate;
	}
	
}
