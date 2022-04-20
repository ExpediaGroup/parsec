import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

export interface ExecuteQueryArgs {
  query: string;
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
      })
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
