// ====================================================================== 
// \title  ParamsTopologyAc.hpp
// \author Generated by fpp-to-cpp
// \brief  hpp file for Params topology
//
// \copyright
// Copyright (c) 2021 California Institute of Technology.
// U.S. Government sponsorship acknowledged.
// All rights reserved.
// ======================================================================

#ifndef M_ParamsTopologyAc_HPP
#define M_ParamsTopologyAc_HPP

#include "C.hpp"
#include "ParamsTopologyDefs.hpp"

namespace M {

  // ----------------------------------------------------------------------
  // Constants
  // ----------------------------------------------------------------------

  namespace BaseIds {
    enum {
      c1 = 0x100,
      c2 = 0x200,
    }
  }

  namespace InstanceIds {
    enum {
      c1,
      c2,
    }
  }

  // ----------------------------------------------------------------------
  // Public interface functions
  // ----------------------------------------------------------------------

  //! Set up the topology
  void setup(
      const TopologyState& state //!< The topology state
  );

  //! Tear down the topology
  void teardown(
      const TopologyState& state //!< The topology state
  );

}

#endif