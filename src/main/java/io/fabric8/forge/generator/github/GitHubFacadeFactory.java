package io.fabric8.forge.generator.github;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.fabric8.forge.generator.AttributeMapKeys;
import io.fabric8.forge.generator.cache.CacheFacade;
import io.fabric8.forge.generator.cache.CacheNames;
import io.fabric8.forge.generator.git.GitAccount;
import io.fabric8.forge.generator.git.GitSecretNames;
import org.infinispan.Cache;
import org.jboss.forge.addon.ui.context.UIContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory to create a GitHubFacade with.
 */
@ApplicationScoped
public class GitHubFacadeFactory {
    private static final transient Logger LOG = LoggerFactory.getLogger(AbstractGitHubStep.class);
    private Cache<String, GitAccount> accountCache;

    @Inject
    public GitHubFacadeFactory(CacheFacade cacheManager) {
        this.accountCache = cacheManager.getCache(CacheNames.GITHUB_ACCOUNT_FROM_SECRET);
    }

    protected GitHubFacade createGitHubFacade(UIContext context) {
        return createGitHubFacade(context, this.accountCache);
    }

    public GitHubFacade createGitHubFacade(UIContext context, Cache<String, GitAccount> accountCache) {
        GitAccount details = (GitAccount) context.getAttributeMap().get(AttributeMapKeys.GIT_ACCOUNT);
        if (details == null) {
            details = GitAccount.loadFromSaaS(context);
        }
        return new GitHubFacade(details);
    }
}
