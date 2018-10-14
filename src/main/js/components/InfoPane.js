import React from 'react';
import { withContext } from '../Context';
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
    info: { city, isLoopable, routesTo, size, edges, eachJump },
    selected,
    netId
  }
}) => (
  <div className={'info'}>
    <h2>
      {city} {isLoopable && '∞'}
    </h2>
    <h4>
      <span
        style={{
          color: toRGB(netId),
          WebkitTextStroke: '0.05px black'
        }}
      >
        ❉
      </span>
      {` netID-${netId}`}
    </h4>
    {size} cities, {edges} routes
    <RoutesList desc={'Direct routes to:'} routes={routesTo} />
    {selected &&
      eachJump
        .slice(1)
        .map((j, i) => (
          <RoutesList key={j[0]} desc={`In ${i + 2} trips:`} routes={j} />
        ))}
  </div>
);

export default withContext(InfoPane);
