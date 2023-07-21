import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { nanoid } from 'nanoid';

import { historySlice } from './history.slice';

export interface ExecuteQueryArgs {
  query: string;
  _internal?: boolean;
}

export const parsecApi = createApi({
  reducerPath: 'parsecApi',
  baseQuery: fetchBaseQuery({ baseUrl: '/api/' }),
  endpoints: (builder) => ({
    executeQuery: builder.mutation<any, ExecuteQueryArgs>({
      query: (body: ExecuteQueryArgs) => ({
        url: `execute`,
        method: 'POST',
        body
      }),
      onQueryStarted: async (body, { dispatch, queryFulfilled }) => {
        // Ignore internal queries
        if (body._internal) {
          return;
        }

        const id = nanoid();
        const { query } = body;

        dispatch(
          historySlice.actions.addHistory({
            id,
            query,
            status: 'running'
          })
        );

        // Wait for the query to finish
        const result = await queryFulfilled;

        const { data } = result;

        dispatch(
          historySlice.actions.updateHistory({
            id,
            query,
            status: data.errors.length > 0 ? 'error' : 'success',
            executionResult: data
          })
        );
      }
    }),
    validateQuery: builder.mutation<any, ExecuteQueryArgs>({
      query: (body: ExecuteQueryArgs) => ({
        url: `validate`,
        method: 'POST',
        body
      })
    })
  })
});

export const { useExecuteQueryMutation, useValidateQueryMutation } = parsecApi;
