package guru.qa.rococo.service.impl;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.painting.PaintingEntity;
import guru.qa.rococo.data.repository.PaintingRepository;
import guru.qa.rococo.data.repository.implRepository.painting.PaintingRepositoryHibernate;
import guru.qa.rococo.data.tpl.XaTransactionTemplate;
import guru.qa.rococo.model.rest.PaintingJson;
import guru.qa.rococo.service.PaintingClient;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.NotFoundException;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PaintingDbClient implements PaintingClient {

    private static final Config CFG = Config.getInstance();
    private final PaintingRepository paintingRepository = new PaintingRepositoryHibernate();
    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(CFG.paintingJdbcUrl());


    @Override
    @Nonnull
    @Step("Create painting using SQL")
    public PaintingJson createPainting(PaintingJson paintingJson) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(
                () -> PaintingJson.fromEntity(
                        paintingRepository.create(PaintingEntity.fromJson(paintingJson))
                )
        ));
    }

    @Override
    public PaintingJson getPaintingByTitle(@NotNull String title) {
        return PaintingJson.fromEntity(paintingRepository.findByTitle(title)
                .orElseThrow(() -> new NotFoundException("Painting with title: '" + title + "' not found"))
        );
    }
}
