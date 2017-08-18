package me.ezjs.core.web.web.controller;

import me.ezjs.core.exception.AppException;
import me.ezjs.core.exception.ZeroAppException;
import me.ezjs.core.web.util.Result;
import me.ezjs.core.web.util.ResultStatus;
import me.ezjs.core.web.web.binder.ZeroDateEditor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 控制层基本类.
 * <p>
 * <p>
 * 每一个可访问的方法必须通过@RequestMapping注解写明访问地址
 * <p>
 * 对于dispatcher内为单独模块的,不需要在类上加@RequestMapping注解,最终的访问地址就是方法地址;
 * <p>
 * 对于dispatcher内还分不同模块的,可以在类上再定义一个地址集合,最终的访问地址是类地址+方法地址;
 * <p>
 * 关于在JSP页面中写请求地址,有两种方式:
 * <p>
 * 一个是绝对地址,这个比较简单,但是地址较长;
 * <p>
 * 另一个是相对地址,这个写起来比较方便,
 * 对于上面第一种情况(dispatcher内为单独模块),在dispatcher内部调用时建议采用相对地址,方便简单,直接写方法上的地址即可.
 * 对于上面第二种情况,可根据自己情况选择.
 * <p>
 * 但是对于跨dispatcher的请求,必须采用绝对地址.
 * <p>
 * 访问地址的后缀一律采用.action(具体可以在web.xml中配置)来请求controller,
 * <p>
 * 因为只有为.action地址的请求才会经过openSessionInView,
 * 不需要进行数据库操作的且没有业务逻辑处理的请求可直接写在所在的dispatcher*-servlet.xml中.
 *
 * @author zhujs
 */
public abstract class BaseAction {

    private final Log log = LogFactory.getLog(getClass());

    protected void initBinder(DataBinder binder, boolean nothing) {

        binder.registerCustomEditor(Date.class, new ZeroDateEditor());

        // the below string type has been supported already.
        // this.defaultEditors.put(Charset.class, new CharsetEditor());
        // this.defaultEditors.put(Class.class, new ClassEditor());
        // this.defaultEditors.put(Class[].class, new ClassArrayEditor());
        // this.defaultEditors.put(Currency.class, new CurrencyEditor());
        // this.defaultEditors.put(File.class, new FileEditor());
        // this.defaultEditors.put(InputStream.class, new InputStreamEditor());
        // this.defaultEditors.put(InputSource.class, new InputSourceEditor());
        // this.defaultEditors.put(Locale.class, new LocaleEditor());
        // this.defaultEditors.put(Pattern.class, new PatternEditor());
        // this.defaultEditors.put(Properties.class, new PropertiesEditor());
        // this.defaultEditors.put(Resource[].class, new
        // ResourceArrayPropertyEditor());
        // this.defaultEditors.put(TimeZone.class, new TimeZoneEditor());
        // this.defaultEditors.put(URI.class, new URIEditor());
        // this.defaultEditors.put(URL.class, new URLEditor());
        // this.defaultEditors.put(UUID.class, new UUIDEditor());
        //
        // // Default instances of collection editors.
        // // Can be overridden by registering custom instances of those as
        // custom editors.
        // this.defaultEditors.put(Collection.class, new
        // CustomCollectionEditor(Collection.class));
        // this.defaultEditors.put(Set.class, new
        // CustomCollectionEditor(Set.class));
        // this.defaultEditors.put(SortedSet.class, new
        // CustomCollectionEditor(SortedSet.class));
        // this.defaultEditors.put(List.class, new
        // CustomCollectionEditor(List.class));
        // this.defaultEditors.put(SortedMap.class, new
        // CustomMapEditor(SortedMap.class));
        //
        // // Default editors for primitive arrays.
        // this.defaultEditors.put(byte[].class, new ByteArrayPropertyEditor());
        // this.defaultEditors.put(char[].class, new CharArrayPropertyEditor());
        //
        // // The JDK does not contain a default editor for char!
        // this.defaultEditors.put(char.class, new CharacterEditor(false));
        // this.defaultEditors.put(Character.class, new CharacterEditor(true));
        //
        // // Spring's CustomBooleanEditor accepts more flag values than the
        // JDK's default editor.
        // this.defaultEditors.put(boolean.class, new
        // CustomBooleanEditor(false));
        // this.defaultEditors.put(Boolean.class, new
        // CustomBooleanEditor(true));
        //
        // // The JDK does not contain default editors for number wrapper types!
        // // Override JDK primitive number editors with our own
        // CustomNumberEditor.
        // this.defaultEditors.put(byte.class, new
        // CustomNumberEditor(Byte.class, false));
        // this.defaultEditors.put(Byte.class, new
        // CustomNumberEditor(Byte.class, true));
        // this.defaultEditors.put(short.class, new
        // CustomNumberEditor(Short.class, false));
        // this.defaultEditors.put(Short.class, new
        // CustomNumberEditor(Short.class, true));
        // binder.registerCustomEditor(int.class, new AsdIntEditor());
        // this.defaultEditors.put(Integer.class, new
        // CustomNumberEditor(Integer.class, true));
        // this.defaultEditors.put(long.class, new
        // CustomNumberEditor(Long.class, false));
        // this.defaultEditors.put(Long.class, new
        // CustomNumberEditor(Long.class, true));
        // this.defaultEditors.put(float.class, new
        // CustomNumberEditor(Float.class, false));
        // this.defaultEditors.put(Float.class, new
        // CustomNumberEditor(Float.class, true));
        // this.defaultEditors.put(double.class, new
        // CustomNumberEditor(Double.class, false));
        // this.defaultEditors.put(Double.class, new
        // CustomNumberEditor(Double.class, true));
        // this.defaultEditors.put(BigDecimal.class, new
        // CustomNumberEditor(BigDecimal.class, true));
        // this.defaultEditors.put(BigInteger.class, new
        // CustomNumberEditor(BigInteger.class, true));
        //
        // // Only register config value editors if explicitly requested.
        // if (this.configValueEditorsActive) {
        // StringArrayPropertyEditor sae = new StringArrayPropertyEditor();
        // this.defaultEditors.put(String[].class, sae);
        // this.defaultEditors.put(short[].class, sae);
        // this.defaultEditors.put(int[].class, sae);
        // this.defaultEditors.put(long[].class, sae);
        // }
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        initBinder(binder, true);
    }

    @ExceptionHandler(AppException.class)
    public Result handleAppException(HttpServletRequest req, Exception ex) {
        log.error("Request: " + req.getRequestURL() + ", message: " + ex, ex);
        return Result.error(ResultStatus.APPLICATION_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(ZeroAppException.class)
    public Result handleZeroAppException(HttpServletRequest req, Exception ex) {
        log.error("Request: " + req.getRequestURL() + ", message: " + ex, ex);
        return Result.error(ResultStatus.FRAME_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(HttpServletRequest req, Exception ex) {
        log.error("Request: " + req.getRequestURL() + ", message: " + ex, ex);
        return Result.error(ResultStatus.RUNTIME_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(HttpServletRequest req, Exception ex) {
        log.error("Request: " + req.getRequestURL() + ", message: " + ex, ex);
        return Result.error(ResultStatus.SERVER_ERROR, ex.getMessage());
    }

    /**
     * 确定的对象封装.
     *
     * @param request
     */
    protected BindingResult bind(Object target, ServletRequest request) {
        DataBinder binder = new DataBinder(target);
        MutablePropertyValues mpvs = new ServletRequestParameterPropertyValues(request);
        binder.bind(mpvs);
        BindingResult result = binder.getBindingResult();
        if (result.getErrorCount() > 0) {
            for (ObjectError error : result.getAllErrors()) {
                if (log.isDebugEnabled()) {
                    log.debug("failed to bind:" + error.getObjectName());
                }
            }
        }

        return result;
    }

}
