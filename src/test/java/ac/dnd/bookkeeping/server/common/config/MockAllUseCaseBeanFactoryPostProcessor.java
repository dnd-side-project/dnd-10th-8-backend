package ac.dnd.bookkeeping.server.common.config;

import ac.dnd.bookkeeping.server.global.annotation.UseCase;
import org.junit.platform.commons.util.ClassFilter;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import static org.mockito.Mockito.mock;

public class MockAllUseCaseBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
        final BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        final ClassFilter classFilter = ClassFilter.of(it -> it.isAnnotationPresent(UseCase.class));
        ReflectionUtils.findAllClassesInPackage("ac.dnd.bookkeeping.server", classFilter)
                .forEach(it -> {
                    final BeanDefinition bean = BeanDefinitionBuilder.genericBeanDefinition(it).getBeanDefinition();
                    registry.registerBeanDefinition(it.getSimpleName(), bean);
                    beanFactory.registerSingleton(it.getSimpleName(), mock(it));
                });
    }
}
