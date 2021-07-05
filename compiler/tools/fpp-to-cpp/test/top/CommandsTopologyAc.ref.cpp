// ====================================================================== 
// \title  CommandsTopologyAc.cpp
// \author Generated by fpp-to-cpp
// \brief  cpp file for Commands topology
//
// \copyright
// Copyright (c) 2021 California Institute of Technology.
// U.S. Government sponsorship acknowledged.
// All rights reserved.
// ======================================================================

#include "CommandsTopologyAc.hpp"

namespace M {

  namespace {

    // ----------------------------------------------------------------------
    // Component instances
    // ----------------------------------------------------------------------

    // c1
    C c1(FW_OPTIONAL_NAME("c1"));

    // c2
    C c2(FW_OPTIONAL_NAME("c2"));

    // ----------------------------------------------------------------------
    // Private functions
    // ----------------------------------------------------------------------

    // Initialize components
    void initComponents(const TopologyState& state) {
      c1.init(InstanceIDs::c1);
      c2.init(InstanceIDs::c2);
    }

    // Set component base IDs
    void setBaseIds() {
      c1.setidBase(0x100);
      c2.setidBase(0x200);
    }

    // Register commands
    void regCommands() {
      c1.regCommandsSpecial();
      c2.regCommands();
    }

  }

  // ----------------------------------------------------------------------
  // Public interface functions
  // ----------------------------------------------------------------------

  setup(const TopologyState& state) {

  }

  teardown(const TopologyState& state) {

  }

}