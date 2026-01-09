package br.com.alpha7.server.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * <b>Descrição:</b><br>
 * Representa uma editora cadastrada no sistema.
 * 
 * <p>
 * <b>Regras de negócio;</b>
 * <ul>
 * 	<li>O nome da editora é obrigatório e único.</li>
 * 	<li>A data de registro é automaticamente preenchida no momento da persistência.</li>
 * </ul>
 * 
 * @author Fernando Kramer De Souza.
 * @since 1.0.0
 */
@Entity
@Table(name = "publishers")
public class Publisher {

	/**
	 * dentificador único da editora.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * Nome da editora.
	 * <p>
	 * Deve ser único e não pode ser nulo.
	 */
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	/**
	 * Data e hora do registro do autor no sistema.
	 * <p>
	 * Este valor é atribuído automaticamente antes da persistência.
	 */
	@Column(name = "registration_date", nullable = false)
	private LocalDateTime registrationDate;
	
	/**
	 * Relação de livros associados com á editora.
	 * <p>
	 * Relacionamento muitos-para-muitos com {@link Book},
	 * mapeado pelo lado da entidade de livros.
	 */
	@ManyToMany(mappedBy = "publishers")
    private List<Book> books = new ArrayList<>();

	public Publisher() {

	}
	
	/**
	 * Callback executado automaticamente antes da entidade ser persistida.
	 * <p>
	 * Responsável por definir a data de registro da editora.
	 * 
	 * @since 1.0.0
	 */
	@PrePersist
	private void prePersist() {
		this.registrationDate = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
