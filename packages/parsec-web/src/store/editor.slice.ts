import type { PayloadAction } from '@reduxjs/toolkit';
import { createSlice } from '@reduxjs/toolkit';
import { nanoid } from 'nanoid';

export enum TabType {
  Home = 'home',
  Query = 'query',
  Reference = 'reference'
}

export enum EditorLayout {
  DockBottom = 'dockBottom',
  DockRight = 'dockRight'
}

export interface BaseTabState {
  tabKey: string;
  type: TabType;
  label: string;
}

export interface QueryTabState extends BaseTabState {
  query: string;
  isRunning: boolean;
  isFresh?: boolean;
}

export type TabState = BaseTabState | QueryTabState;

export const isHomeTab = (tab: TabState): tab is BaseTabState => {
  return tab.type === TabType.Home;
};

export const isQueryTab = (tab: TabState): tab is QueryTabState => {
  return tab.type === TabType.Query;
};

export interface EditorState {
  tabs: TabState[];
  selectedTabIndex: number;
  lastTabNumber: number;
  layout: EditorLayout;
}

const initialState: EditorState = {
  tabs: [
    {
      tabKey: nanoid(),
      type: TabType.Home,
      label: 'Welcome'
    }
  ],
  selectedTabIndex: 0,
  lastTabNumber: 1,
  layout: EditorLayout.DockBottom
};

export const editorSlice = createSlice({
  name: 'editor',
  initialState,
  reducers: {
    setSelectedTab(state, action: PayloadAction<number>) {
      state.selectedTabIndex = action.payload;
    },
    toggleLayout(state) {
      state.layout = state.layout === EditorLayout.DockBottom ? EditorLayout.DockRight : EditorLayout.DockBottom;
    },
    updateTabLabel(state, action: PayloadAction<{ tabKey: string; label: string }>) {
      const { tabKey, label } = action.payload;
      const tab = state.tabs.find((t) => t.tabKey === tabKey);
      if (tab) {
        tab.label = label;
      }
    },
    updateTabQuery(state, action: PayloadAction<{ tabKey: string; query: string }>) {
      const { tabKey, query } = action.payload;
      const tab = state.tabs.find((t) => t.tabKey === tabKey);
      if (tab && isQueryTab(tab)) {
        tab.query = query;
      }
    },
    updateQueryTab(state, action: PayloadAction<Partial<QueryTabState>>) {
      const { tabKey, ...rest } = action.payload;
      const tab = state.tabs.find((t) => t.tabKey === tabKey);
      if (tab && isQueryTab(tab)) {
        Object.assign(tab, rest);
      }
    },
    openNewQueryTab(state) {
      const nextTabNumber = state.lastTabNumber + 1;
      state.tabs.push({
        tabKey: nanoid(),
        type: TabType.Query,
        label: `Untitled ${nextTabNumber}`,
        query: `input mock | set @index = ${nextTabNumber}`,
        isRunning: false,
        isFresh: true
      });
      state.lastTabNumber = nextTabNumber;
    },
    openNewHomeTab(state) {
      state.tabs.push({
        tabKey: nanoid(),
        type: TabType.Home,
        label: 'Welcome'
      });
      state.selectedTabIndex = state.tabs.length - 1;
    },
    closeTab(state, action: PayloadAction<{ tabKey: string }>) {
      const { tabKey } = action.payload;
      state.tabs = state.tabs.filter((tab) => tab.tabKey !== tabKey);

      if (state.selectedTabIndex >= state.tabs.length) {
        state.selectedTabIndex = state.tabs.length - 1;
      }
    }
  }
});
