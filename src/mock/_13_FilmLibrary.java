package mock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
interface IFilm {
    String getTitle();
    void setTitle(String title);
    String getDirector();
    void setDirector(String director);
    int getYear();
    void setYear(int year);
}

class Film implements IFilm{
    String title;
    String director;
    int year;

    public Film(String title, String director, int year) {
        this.title = title;
        this.director = director;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

interface IFilmLibrary{
    void addFilm(IFilm film);
    void removeFilm(String title);
    List<IFilm> searchFilms(String query);
    List<IFilm> getFilms();
    int getTotalFilmCount();

}

class FilmLibrary implements IFilmLibrary{
    List<IFilm> films = new ArrayList<>();

    @Override
    public void addFilm(IFilm film) {
        films.add(film);
    }

    @Override
    public void removeFilm(String title) {
        films  = films.stream().filter((IFilm obj) -> !obj.getTitle().equals(title)).collect(Collectors.toList());
    }

    @Override
    public List<IFilm> searchFilms(String query) {
        return films.stream().filter((IFilm obj)-> obj.getTitle().equals(query) || obj.getDirector().equals(query)).collect(Collectors.toList());
    }

    @Override
    public List<IFilm> getFilms() {
        return new ArrayList<>(films);
    }

    @Override
    public int getTotalFilmCount() {
        return films.size();
    }
}

public class _13_FilmLibrary {
    public static void main(String[] args) {
        IFilmLibrary library = new FilmLibrary();

        // ── Seed Data ────────────────────────────────────────────────
        library.addFilm(new Film("Inception",         "Christopher Nolan", 2010));
        library.addFilm(new Film("The Dark Knight",   "Christopher Nolan", 2008));
        library.addFilm(new Film("Interstellar",      "Christopher Nolan", 2014));
        library.addFilm(new Film("Parasite",          "Bong Joon-ho",      2019));
        library.addFilm(new Film("Pulp Fiction",      "Quentin Tarantino", 1994));

        // ── 1. Verify initial count ───────────────────────────────────
        assert library.getTotalFilmCount() == 5
                : "FAIL: Expected 5 films, got " + library.getTotalFilmCount();
        System.out.println("✔ Initial film count: " + library.getTotalFilmCount());

        // ── 2. getFilms() returns a defensive copy (not the internal list) ──
        List<IFilm> snapshot = library.getFilms();
        snapshot.clear(); // mutate the returned list
        assert library.getTotalFilmCount() == 5
                : "FAIL: getFilms() exposed internal list — defensive copy broken";
        System.out.println("✔ getFilms() returns a defensive copy");

        // ── 3. Search by title ────────────────────────────────────────
        List<IFilm> byTitle = library.searchFilms("Inception");
        assert byTitle.size() == 1 && byTitle.get(0).getTitle().equals("Inception")
                : "FAIL: Title search returned unexpected results: " + byTitle.size();
        System.out.println("✔ Search by title 'Inception': found " + byTitle.size() + " film(s)");

        // ── 4. Search by director ─────────────────────────────────────
        List<IFilm> byDirector = library.searchFilms("Christopher Nolan");
        assert byDirector.size() == 3
                : "FAIL: Director search expected 3, got " + byDirector.size();
        System.out.println("✔ Search by director 'Christopher Nolan': found " + byDirector.size() + " film(s)");

        // ── 5. Search with no match ───────────────────────────────────
        List<IFilm> noMatch = library.searchFilms("Avatar");
        assert noMatch.isEmpty()
                : "FAIL: Expected empty result for 'Avatar', got " + noMatch.size();
        System.out.println("✔ Search with no match returns empty list");

        // ── 6. Remove an existing film ────────────────────────────────
        library.removeFilm("Parasite");
        assert library.getTotalFilmCount() == 4
                : "FAIL: Expected 4 after removal, got " + library.getTotalFilmCount();
        assert library.searchFilms("Parasite").isEmpty()
                : "FAIL: 'Parasite' still found after removal";
        System.out.println("✔ removeFilm('Parasite'): count now " + library.getTotalFilmCount());

        // ── 7. Remove a non-existent film (should be a no-op) ─────────
        library.removeFilm("Ghost Film");
        assert library.getTotalFilmCount() == 4
                : "FAIL: Count changed after removing non-existent film";
        System.out.println("✔ Removing non-existent film is a safe no-op");

        // ── 8. Add a duplicate and verify count ───────────────────────
        library.addFilm(new Film("Inception", "Christopher Nolan", 2010));
        assert library.getTotalFilmCount() == 5
                : "FAIL: Duplicate add not reflected in count";
        System.out.println("✔ Duplicate add allowed, count: " + library.getTotalFilmCount());

        System.out.println("\n✅ All checks passed.");

        // ── BUG REPORT ────────────────────────────────────────────────
        System.out.println("""
            \n⚠ Known Bug in searchFilms():
              Current:  obj.title.equals(query) && obj.director.equals(query)
              Fixed:    obj.title.equals(query) || obj.director.equals(query)
              Impact:   Director search (Test 4) will return 0 results instead of 3.
            """);
    }
}
