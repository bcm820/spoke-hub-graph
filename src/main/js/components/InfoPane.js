import React from 'react';
import { withContext } from '../Context';
import { toJS, show } from '../../scala-js/transit-fastopt';
import { toRGB } from '../helpers';

const RoutesList = ({ desc, routes }) => (
  <div style={{ marginTop: 10 }}>
    {desc}
    <ul>
      {routes.map(c => (
        <li key={c}>{c}</li>
      ))}
    </ul>
  </div>
);

const InfoPane = ({
  ctx: {
    info: { city, net, size, edges },
    selected,
    netId
  }
}) => (
  <div className={'info'}>
    <h2>
      <span
        style={{
          color: toRGB(netId),
          WebkitTextStroke: '0.05px black'
        }}
      >
        ❉
      </span>
      {` ${city}`} {net.isLoopable(city) && '∞'}
    </h2>
    {size} cities, {edges} routes
    <RoutesList
      desc={'Direct routes to:'}
      routes={toJS(show(net.routes(city)))}
    />
    {selected &&
      size > 2 &&
      toJS(net.eachJump(city))
        .map(s => toJS(show(s)))
        .slice(1)
        .map((j, i) => (
          <RoutesList key={j[0]} desc={`In ${i + 2} trips:`} routes={j} />
        ))}
  </div>
);

export default withContext(InfoPane);
