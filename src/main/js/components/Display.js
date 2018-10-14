import { toJS, show } from '../../scala-js/transit-fastopt';
import { toRGB } from '../helpers';

import React from 'react';
import { withContext } from '../Context';

import {
  VictoryGroup,
  VictoryTheme,
  VictoryScatter,
  VictoryLabel,
  Point
} from 'victory';

class Display extends React.Component {
  /**
   * Selects a city onClick so that its info is displayed after onMouseOut.
   * Deselect by clicking the same city again, or a city in the same network.
   */
  handleCityClick = (city, net) => {
    const { cityId, setCtx } = this.props.ctx;
    const toggle = city === cityId;
    if (!toggle) this.setInfo(city, net);
    const plotPoints = this.updatePlotPoints(cityId, city, toggle);
    setCtx({
      cityId: toggle ? null : city,
      selected: toggle ? null : city,
      plotPoints
    });
  };

  /**
   * Updates the select city's symbol (restores a deselected city).
   */
  updatePlotPoints = (prev, next, toggle) => {
    const { plotPoints } = this.props.ctx;
    let prevSymbol;
    if (toggle) {
      prevSymbol = plotPoints[prev].symbol === 'circle';
      plotPoints[prev].symbol = prevSymbol ? 'star' : 'circle';
    } else {
      if (prev) plotPoints[prev].symbol = 'circle';
      plotPoints[next].symbol = 'star';
    }
    return plotPoints;
  };

  /**
   * Handles state updates for the information pane that displays
   * responses to queries made by calling Network methods.
   */
  setInfo = (city, net) => {
    const { setCtx } = this.props.ctx;
    setCtx({
      netId: net.id,
      info: {
        city,
        routesTo: toJS(show(net.routes(city))),
        eachJump: toJS(net.eachJump(city)).map(s => toJS(show(s))),
        isLoopable: net.isLoopable(city),
        size: net.size,
        edges: net.edges
      }
    });
  };

  render() {
    const {
      ctx: { netId, plotPoints, transitSys, selected, clearInfo }
    } = this.props;
    const networks = toJS(transitSys.networks);
    return (
      <div className={'display'}>
        <VictoryGroup theme={VictoryTheme.material}>
          {networks.map(net => {
            const currentNetwork = netId ? netId === net.id : true;
            return (
              <VictoryScatter
                key={net.id}
                size={netId === net.id ? 8 : 4}
                style={{
                  data: {
                    fill: toRGB(net.id),
                    stroke: 'black',
                    strokeWidth: netId === net.id ? 2 : 1.25,
                    opacity: currentNetwork ? 1 : 0.08,
                    zIndex: currentNetwork ? -10 : -20
                  },
                  labels: {
                    fontSize: netId === net.id ? 12 : 8,
                    fill: 'black',
                    opacity: currentNetwork ? 1 : 0.08
                  }
                }}
                data={toJS(net.cities).map(c => plotPoints[c])}
                dataComponent={
                  <Point className={currentNetwork ? 'point' : 'other'} />
                }
                labelComponent={<VictoryLabel dy={currentNetwork ? -6 : -2} />}
                events={[
                  {
                    target: 'data',
                    eventHandlers: {
                      onMouseOver: () => [
                        {
                          target: 'labels',
                          mutation: ({ text }) =>
                            !selected && this.setInfo(text, net)
                        }
                      ],
                      onMouseOut: () => [
                        { mutation: () => !selected && clearInfo() }
                      ],
                      onClick: () => [
                        {
                          target: 'labels',
                          mutation: ({ text }) =>
                            (!selected || (selected && netId === net.id)) &&
                            this.handleCityClick(text, net)
                        }
                      ]
                    }
                  }
                ]}
              />
            );
          })}
        </VictoryGroup>
      </div>
    );
  }
}

export default withContext(Display);
