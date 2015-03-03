# VolleyPlusExtension

##支持读取本地json文件(一般用在接口开发完成前的客户端数据填充)##

使用方法就是初始化requestQueue时候采用LocalStack作数据请求，开发者使用该方法不用关心其内部实现，加载本地json文件的逻辑可以视为与网络请求无异，

代码如下：
```java
LocalStack localStack = new LocalStack(getApplicationContext(), LocalStack.FileType.RAW);
RequestQueue queue ＝ Volley.newRequestQueue(getApplicationContext(),localStack);

```


##支持自定义请求策略##

###只读缓存###
```java
request.setRequestType(StrategyRequest.RequestType.CACHE);
RequestHelper.getInstance().doRequest(queue,request);
```
###先读缓存再读网络###
```java
request.setRequestType(StrategyRequest.RequestType.CACHE_BEFORE_NETWORK);
RequestHelper.getInstance().doRequest(queue,request);
```
###只读网络###
```java
request.setRequestType(StrategyRequest.RequestType.NETWORK);
RequestHelper.getInstance().doRequest(queue,request);
```
###先读网络如果失败再读缓存###

```java
request.setRequestType(StrategyRequest.RequestType.NETWORK_BEFORE_CACHE);
RequestHelper.getInstance().doRequest(queue,request);
```
错误回调使用StrategyRequest.CustomCacheErrorListener，在请求失败回调中读取缓存
```java
 @Override
  public void netWorkErrorReadCache(StrategyRequest<?> request) {
      Toast.makeText(getActivity(), "netWorkErrorReadCache", Toast.LENGTH_LONG).show();
      RequestHelper.getInstance().doRequest(queue,((StrategyRequest)request));//再次读取缓存
  }
```
