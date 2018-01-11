package com.hss01248.http.interfaces;

/**
 * Created by hss on 2017/12/28.
 */

public interface IResponseParser<R,C> {

    void parseSuccessResponse(R response,C call);

    void paseFailture(R response, C call, Throwable e);
}
