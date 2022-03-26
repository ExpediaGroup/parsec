import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

// Define a service using a base URL and expected endpoints
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

// Export hooks for usage in functional components, which are
// auto-generated based on the defined endpoints
export const { useExecuteQueryMutation, useValidateQueryMutation } = parsecApi;
