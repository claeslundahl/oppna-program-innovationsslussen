package se.vgregion.service.innovationsslussen.repository.ideafile;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaFile;

/**
 * Repository interface for managing {@code Idea}s.
 * 
 * @author Simon Göransson
 * @company Monator Technologies AB
 */
public interface IdeaFileRepository extends Repository<IdeaFile, Long> {

    /**
     * Find an {@link Idea} with urlTitle specified
     * 
     * @param id
     *            the id
     * @return an {@link Idea}
     */
    @Override
    IdeaFile find(Long id);

}
