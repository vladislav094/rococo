package guru.qa.rococo.data.repository.implRepository.artist;

import guru.qa.rococo.data.dao.artist.ArtistDao;
import guru.qa.rococo.data.dao.implDao.artist.ArtistDaoSpringJdbc;
import guru.qa.rococo.data.entity.artist.ArtistEntity;
import guru.qa.rococo.data.repository.ArtistRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class ArtistRepositorySpringJdbc implements ArtistRepository {

    private final ArtistDao artistDao = new ArtistDaoSpringJdbc();

    @NotNull
    @Override
    public ArtistEntity create(ArtistEntity artist) {
        return artistDao.create(artist);
    }

    @Override
    public Optional<ArtistEntity> findById(@NotNull UUID id) {
        return artistDao.findById(id);
    }

    @Override
    public Optional<ArtistEntity> findByName(@NotNull String name) {
        return artistDao.findByName(name);
    }
}
