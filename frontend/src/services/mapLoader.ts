import AMapLoader from '@amap/amap-jsapi-loader';

declare global {
  interface Window {
    _AMapSecurityConfig?: {
      securityJsCode: string;
    };
  }
}

const AMAP_KEY = import.meta.env.VITE_AMAP_KEY as string | undefined;
const AMAP_SECRET = import.meta.env.VITE_AMAP_SECRET as string | undefined;

if (AMAP_SECRET) {
  window._AMapSecurityConfig = {
    securityJsCode: AMAP_SECRET,
  };
}

export const loadMap = async () => {
  if (!AMAP_KEY) {
    throw new Error('未配置高德地图 Key，请在环境变量 VITE_AMAP_KEY 中设置');
  }
  return AMapLoader.load({
    key: AMAP_KEY,
    version: '2.0',
    plugins: ['AMap.Scale', 'AMap.ToolBar', 'AMap.PlaceSearch'],
  });
};


