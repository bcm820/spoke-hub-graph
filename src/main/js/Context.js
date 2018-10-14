import React from 'react';

const Context = React.createContext();

export const CtxProvider = Context.Provider;

export const withContext = Component => props => (
  <Context.Consumer children={ctx => <Component ctx={ctx} {...props} />} />
);
