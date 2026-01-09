package br.com.alpha7.server.infrastructure.validation;

import br.com.alpha7.server.infrastructure.exception.InvalidIsbnException;

/**
 * <b>Descrição:</b><br>
 * Classe utilitária responsável pela validação de códigos ISBN.
 *
 * <p>
 * Oferece validação para os formatos ISBN-10 e ISBN-13 seguindo
 * as regras oficiais de cálculo de dígito verificador.
 * Também realiza normalização removendo espaços, hífens e caracteres
 * inválidos antes da validação.
 * </p>
 *
 * <h3>Comportamento</h3>
 * <ul>
 *     <li>Permite ISBN com hífens, espaços ou formatados</li>
 *     <li>Normaliza o valor mantendo apenas números e o caractere 'X'</li>
 *     <li>Valida ISBN-10 e ISBN-13 conforme algoritmos oficiais</li>
 *     <li>Lança exceções de negócio em caso de inconsistência</li>
 * </ul>
 *
 * <h3>Exceções</h3>
 * <ul>
 *     <li>{@link InvalidIsbnException} caso:
 *         <ul>
 *             <li>o ISBN seja nulo ou vazio</li>
 *             <li>não possua 10 ou 13 caracteres válidos</li>
 *             <li>não atenda às regras de validação</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h3>Uso Típico</h3>
 * <pre>
 * String validIsbn = IsbnValidator.validate("978-3-16-148410-0");
 * </pre>
 *
 * <h3>Retorno</h3>
 * Retorna o ISBN normalizado (somente números e 'X' se aplicável).
 *
 * <h3>Design</h3>
 * <ul>
 *     <li>Classe final para evitar herança</li>
 *     <li>Construtor privado para impedir instanciação</li>
 *     <li>Método estático para uso direto</li>
 * </ul>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
public final class IsbnValidator {

	 /** Construtor privado para impedir instanciação. */
	private IsbnValidator() {}
	
    /**
     * Valida um ISBN, normalizando seu valor e aplicando as regras de verificação
     * para ISBN-10 e ISBN-13.
     *
     * @param isbn valor recebido para validação
     * @return ISBN normalizado somente com dígitos e 'X' quando aplicável
     * @throws InvalidIsbnException caso o valor seja inválido ou não atenda às regras
     */
    public static String validate(String isbn) {

        if (isbn == null || isbn.trim().isEmpty()) {
            throw new InvalidIsbnException("ISBN não informado.");
        }

        String normalizedIsbn = isbn.replaceAll("[^0-9Xx]", "");

        if (normalizedIsbn.length() == 10) {
            if (!isValidIsbn10(normalizedIsbn)) {
                throw new InvalidIsbnException("ISBN-10 inválido.");
            }
        } else if (normalizedIsbn.length() == 13) {
            if (!isValidIsbn13(normalizedIsbn)) {
                throw new InvalidIsbnException("ISBN-13 inválido.");
            }
        } else {
            throw new InvalidIsbnException("ISBN deve conter 10 ou 13 dígitos.");
        }

        return normalizedIsbn;
    }
    
    private static boolean isValidIsbn10(String isbn) {
        int sum = 0;

        for (int i = 0; i < 10; i++) {
            char c = isbn.charAt(i);
            int value;

            if (i == 9 && (c == 'X' || c == 'x')) {
                value = 10;
            } else if (Character.isDigit(c)) {
                value = Character.getNumericValue(c);
            } else {
                return false;
            }

            sum += value * (10 - i);
        }

        return sum % 11 == 0;
    }

    private static boolean isValidIsbn13(String isbn) {
        int sum = 0;

        for (int i = 0; i < 13; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        return sum % 10 == 0;
    }
}