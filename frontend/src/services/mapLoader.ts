import { useSettingsStore } from '@/stores/useSettingsStore';

declare global {
  interface Window {
    BMapGL?: BMapGLNamespace;
  }
}

export interface BMapGLNamespace {
  Map: new (container: string | HTMLElement, opts?: Record<string, unknown>) => BMapGLMap;
  Point: new (lng: number, lat: number) => BMapGLPoint;
  Marker: new (point: BMapGLPoint) => BMapGLMarker;
  Label: new (content: string, opts?: { position?: BMapGLPoint; offset?: BMapGLSize }) => BMapGLLabel;
  InfoWindow: new (content: string, opts?: Record<string, unknown>) => BMapGLInfoWindow;
  Size: new (width: number, height: number) => BMapGLSize;
  NavigationControl: new () => unknown;
  ScaleControl: new () => unknown;
}

export interface BMapGLMap {
  centerAndZoom(point: BMapGLPoint, zoom: number): void;
  enableScrollWheelZoom(enable?: boolean): void;
  addControl(control: unknown): void;
  addOverlay(overlay: unknown): void;
  openInfoWindow(infoWindow: BMapGLInfoWindow, point: BMapGLPoint): void;
}

export interface BMapGLPoint {
  lng: number;
  lat: number;
}

export interface BMapGLSize {
  width: number;
  height: number;
}

export interface BMapGLMarker {
  setLabel(label: BMapGLLabel): void;
  addEventListener(event: string, handler: (...args: unknown[]) => void): void;
}

export interface BMapGLLabel {
  setStyle(styles: Record<string, string | number>): void;
}

export interface BMapGLInfoWindow {
  setTitle(title: string): void;
}

let loadingPromise: Promise<BMapGLNamespace> | null = null;
let currentAk: string | undefined;
const SCRIPT_ID = 'baidu-map-gl-script';

export const loadMap = async (): Promise<BMapGLNamespace> => {
  if (window.BMapGL) {
    return window.BMapGL;
  }

  const settingsStore = useSettingsStore();
  const ak = settingsStore.baiduAk || (import.meta.env.VITE_BMAP_AK as string | undefined);

  if (!ak) {
    throw new Error('未配置百度地图 AK，请在页面右上角“API Key 设置”中填写或配置环境变量 VITE_BMAP_AK');
  }

  if (loadingPromise && ak !== currentAk) {
    const existing = document.getElementById(SCRIPT_ID);
    existing?.remove();
    loadingPromise = null;
    currentAk = undefined;
    // @ts-expect-error allow reset
    window.BMapGL = undefined;
  }

  if (!loadingPromise) {
    currentAk = ak;
    loadingPromise = new Promise<BMapGLNamespace>((resolve, reject) => {
      const existing = document.getElementById(SCRIPT_ID);
      if (existing) {
        existing.remove();
      }
      const script = document.createElement('script');
      script.id = SCRIPT_ID;
      script.src = `https://api.map.baidu.com/api?v=1.0&type=webgl&ak=${ak}`;
      script.async = true;
      script.onerror = () => reject(new Error('百度地图脚本加载失败'));
      script.onload = () => {
        if (window.BMapGL) {
          resolve(window.BMapGL);
        } else {
          reject(new Error('百度地图未正确初始化'));
        }
      };
      document.body.appendChild(script);
    });
  }

  return loadingPromise;
};


